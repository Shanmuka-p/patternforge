# PatternForge

> An interactive, live-observable playground for all seven **Gang of Four structural design patterns**, built with **Spring Boot 3** and **Java 17**.

PatternForge is not a textbook — it is a running application. Every call to a pattern's `render()` method emits a real-time event over a STOMP WebSocket, letting you watch the exact execution chain unfold as it happens. Patterns are implemented as plain Java objects inside a rich domain model; the REST API and WebSocket infrastructure expose that domain to any client.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Running the Application (Docker)](#running-the-application-docker)
- [Design Pattern Implementations](#design-pattern-implementations)
- [Architecture Decisions](#architecture-decisions)
- [FAQ / Design Questions](#faq--design-questions)
- [Tech Stack](#tech-stack)

---

## Project Overview

PatternForge demonstrates all seven structural design patterns from the Gang of Four in a single, coherent domain: a **widget-based dashboard renderer**. Each pattern plays a specific, motivated role in that domain — Composite builds the widget tree, Decorator layers visual enhancements, Proxy guards access and defers loading, Bridge decouples rendering strategies, Adapter integrates legacy chart libraries, Facade simplifies multi-subsystem dashboard creation, and Flyweight shares intrinsic ticker state across many stock widgets.

The project's distinguishing feature is its **live call-chain tracer**: a WebSocket feed that broadcasts a `CallChainEvent` (id, patternType, className, methodName, timestamp) every time a traced `render()` executes. This makes the pattern interactions visible and verifiable, transforming abstract design concepts into observable runtime behaviour.

---

## Running the Application (Docker)

The project ships with a multi-stage `Dockerfile` (builds with `eclipse-temurin:17-jdk-alpine`, runs on `eclipse-temurin:17-jre-alpine`) and a `docker-compose.yml` that reads configuration from a `.env` file.

### 1 — Create your environment file

Copy the provided example and adjust values if needed:

```bash
cp .env.example .env
```

Default contents of `.env`:

```env
APP_PORT=8080
SPRING_PROFILE=dev
```

`APP_PORT` controls which host port maps to the container's `8080`. `SPRING_PROFILE` sets `SPRING_PROFILES_ACTIVE` inside the container.

### 2 — Build the image and start the container

```bash
docker-compose up --build
```

The first build downloads Maven dependencies and compiles the source; subsequent builds are faster due to Docker layer caching of the `dependency:go-offline` stage.

To run in detached mode:

```bash
docker-compose up --build -d
```

To stop and remove containers:

```bash
docker-compose down
```

### 3 — Access the application

| Endpoint | URL |
|---|---|
| Application root | <http://localhost:8080> |
| Swagger UI (API explorer) | <http://localhost:8080/swagger-ui.html> |
| OpenAPI JSON spec | <http://localhost:8080/v3/api-docs> |
| WebSocket endpoint (SockJS) | `ws://localhost:8080/ws-patternforge` |
| Live call-chain topic | `/topic/call-chain` |

> **Note:** If you changed `APP_PORT` in `.env`, substitute that port for `8080` in all URLs above.

---

## Design Pattern Implementations

### 1 · Composite

**Package:** `com.example.patternforge.patterns.composite`

`DashboardComponent` is the component interface shared by both `ContainerNode` (composite) and `WidgetNode` (leaf). `ContainerNode` holds an ordered list of child `DashboardComponent`s and recursively calls `render()` on each, aggregating their HTML and CSS into a single `RenderResult`. Cycle detection and self-reference guards are enforced at `add()` time. Every `render()` call in both classes emits a `"Composite"` trace event to the WebSocket before doing any work.

### 2 · Decorator

**Package:** `com.example.patternforge.patterns.decorator`

`WidgetDecorator` is the abstract base decorator that wraps any `DashboardComponent` and delegates all interface calls to it. Concrete decorators — `BorderDecorator`, `HighlightDecorator`, and `LoggingDecorator` — each override `render()` to inject additional HTML or behaviour before or after the delegation. Because `WidgetDecorator.render()` calls `PatternStackTracer.trace` using `this.getClass().getSimpleName()`, every subclass automatically self-identifies in the call-chain stream without any per-subclass instrumentation code.

### 3 · Adapter

**Package:** `com.example.patternforge.patterns.adapter`

Two legacy charting libraries (`LegacyGraphLib` and `OldChartLib`) expose incompatible APIs. `LegacyGraphAdapter` and `OldChartAdapter` each implement the `ChartWidget` target interface, internally translating the standardised `render()` contract into the specific method signatures each legacy library expects. Clients program only against `ChartWidget`; the adapters are transparent.

### 4 · Facade

**Package:** `com.example.patternforge.patterns.facade`

`DashboardManager` is the facade that coordinates four independent subsystems — `LayoutEngine`, `ThemeEngine`, `WidgetRegistry`, and `RenderEngine` — behind a single `createDashboard()` call. The `FacadeController` exposes `DashboardManager` over REST (`POST /api/facade/create-dashboard`), meaning a client can trigger a complete, multi-step dashboard initialisation in one HTTP request without knowing anything about the subsystems involved.

### 5 · Proxy

**Package:** `com.example.patternforge.patterns.proxy`

Two distinct proxy variants are implemented. `LazyWidgetProxy` (Virtual Proxy) defers the expensive construction and data-loading of a `VideoWidget` until `render()` is first called, returning a lightweight skeleton `<div>` in the meantime. `AccessControlProxy` (Protection Proxy) extends `WidgetDecorator` and consults `SessionState.getCurrentRole()` on every `render()` call, returning an "Access Denied" fragment if the role does not match, or delegating to the wrapped component if it does.

### 6 · Bridge

**Package:** `com.example.patternforge.patterns.bridge`

`BridgeWidget` (abstraction) holds a reference to a `WidgetRenderer` (implementation interface). `StandardBridgeWidget` extends `BridgeWidget` and calls `renderer.renderData()` without knowing which renderer is active. Three concrete renderers — `HtmlRenderer`, `JsonRenderer`, and `SvgRenderer` — can be plugged in independently of the widget hierarchy. The `RendererService` acts as a Spring-managed factory for the renderer implementations, allowing the abstraction and implementation to vary along entirely separate axes.

### 7 · Flyweight

**Package:** `com.example.patternforge.patterns.flyweight`

`StockTickerWidget` separates its state into intrinsic (shared, immutable: `tickerSymbol` and `color`, set at construction) and extrinsic (context-specific, mutable: `price` and `change`, supplied per render via `setExtrinsicState()`). A flyweight factory would cache and reuse `StockTickerWidget` instances keyed on their intrinsic state, so thousands of dashboard tickers for the same symbol share a single object's overhead. Each `render()` call emits a `"Flyweight"` trace event before producing the HTML fragment.

---

## Architecture Decisions

### Explicit Manual Instrumentation over Spring AOP

The `PatternStackTracer` traces execution through **direct, hand-placed method calls** — not through Spring AOP proxies or AspectJ pointcuts. This was a deliberate and carefully considered choice.

Spring AOP operates at the proxy boundary: it intercepts method calls on Spring-managed beans by wrapping them in a `JdkDynamicProxy` or CGLIB subclass. This imposes a hard constraint that is fatal for this project — the core pattern classes (`ContainerNode`, `WidgetNode`, `BorderDecorator`, `LazyWidgetProxy`, etc.) are **plain Java objects instantiated by client code**, not Spring beans. AOP pointcuts cannot reach them at all. Even for Spring-managed collaborators, AOP only intercepts calls that cross the proxy boundary; internal `this.render()` calls within the same bean instance are silently bypassed, producing misleading or incomplete traces.

Explicit instrumentation carries none of these limitations. Each `PatternStackTracer.trace(...)` call is placed at the exact execution milestone that is semantically meaningful — for example, inside `LoggingDecorator.render()` *before* `super.render()`, so the trace fires in the correct order relative to the delegation chain. This provides **microscopic, deterministic control** over which execution milestones are broadcast to the `/topic/call-chain` WebSocket topic. The trace is guaranteed to fire regardless of how the object was created, what thread it runs on, or whether it is managed by the Spring container. AOP pointcut unpredictability, proxy overhead, and the risk of missing intra-object calls are eliminated entirely.

---

## FAQ / Design Questions

### Why use an interface for `DashboardComponent` instead of an abstract class?

Interfaces in Java permit **multiple inheritance of type**, which is critical here because `AccessControlProxy` needs to be both a `DashboardComponent` and a `WidgetDecorator`. An abstract class would force a single inheritance chain and make that combination impossible without significant restructuring. Equally important, `DashboardComponent` uses Java's `default` method mechanism to provide stub implementations of `add()`, `remove()`, and `getChildren()` that throw `UnsupportedOperationException` — giving leaf nodes (`WidgetNode`, `StockTickerWidget`) a safe no-op implementation automatically, without requiring them to extend a concrete class. The interface therefore acts as both a type contract and a partial implementation strategy without coupling the hierarchy to a single base class.

### Why are decorators manually instantiated rather than declared as Spring `@Component`s?

Decorators are **stateful, runtime-composed wrappers** — `new BorderDecorator(new HighlightDecorator(new LoggingDecorator(widget)))` is built dynamically based on the specific widget being decorated and the decoration choices active at that moment. Spring's component model is designed for singleton or scoped beans with fixed, wired collaborators; it has no native mechanism for constructing arbitrary decorator stacks around arbitrary target objects at request time. Declaring decorators as `@Component`s would mean each decorator type is a singleton wrapping a fixed target, destroying the entire point of the pattern. Manual instantiation keeps decorators as pure, stateless-wrapper objects that can be composed in any order, around any compatible target, at any point in the application's execution.

### How are WebSocket events pushed from the backend?

When `PatternStackTracer.trace(...)` is called, it constructs a `CallChainEvent` record (populated with a random UUID, the pattern type, class name, method name, and `System.currentTimeMillis()`) and immediately calls `instance.messagingTemplate.convertAndSend("/topic/call-chain", event)`. The `messagingTemplate` is a Spring `SimpMessagingTemplate`, which serialises the record to JSON and delivers it to the STOMP in-memory broker configured in `WebSocketConfig`. Any client that has subscribed to the `/topic/call-chain` destination — via the SockJS WebSocket endpoint at `/ws-patternforge` — receives the event within milliseconds of the originating `render()` call.

### What happens if a mutable class is used for the Flyweight's intrinsic state?

The Flyweight pattern's correctness guarantee rests entirely on intrinsic state being **shared safely across all contexts**. If a flyweight's shared fields are mutable, any one consumer modifying them — for example, changing the `tickerSymbol` of a cached `StockTickerWidget` — immediately corrupts the state seen by every other consumer of that instance, producing data races under concurrent access and logically incorrect output even in single-threaded use. The entire premise of the pattern breaks down: the object can no longer be shared, so none of the memory savings materialise, and the bugs introduced are subtle and difficult to reproduce. In PatternForge, the `CallChainEvent` type used by the tracer is a **Java `record`** — records are inherently immutable by specification, so their fields cannot be mutated after construction. The same discipline is applied to `StockTickerWidget`: its intrinsic fields (`tickerSymbol`, `color`) are `final`, ensuring the shared state is provably unchanged across all render calls regardless of concurrency.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.5 |
| Web | Spring MVC (`spring-boot-starter-web`) |
| WebSocket | Spring WebSocket + STOMP + SockJS (`spring-boot-starter-websocket`) |
| Build | Maven (Maven Wrapper) |
| Containerisation | Docker (multi-stage, `eclipse-temurin:17`) · Docker Compose |
| API Documentation | SpringDoc OpenAPI / Swagger UI |
| Testing | JUnit 5 + Mockito (`spring-boot-starter-test`) |
