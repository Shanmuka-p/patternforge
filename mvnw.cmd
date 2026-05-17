@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Apache Maven Wrapper startup batch script, version 3.2.0
@REM ----------------------------------------------------------------------------

@IF "%__MVNW_ARG0_NAME__%"=="" (SET "MAVEN_JAVA_EXE=%JAVA_HOME%\bin\java.exe") ELSE (SET "MAVEN_JAVA_EXE=%__MVNW_ARG0_NAME__%.exe")
@SET WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
@SET WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

@SET DOWNLOAD_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"

@FOR /F "usebackq tokens=1,2 delims==" %%A IN ("%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties") DO (
    @IF "%%A"=="wrapperUrl" SET DOWNLOAD_URL=%%B
)

@IF EXIST %WRAPPER_JAR% (
    @IF "%MVNW_VERBOSE%"=="true" @ECHO Found %WRAPPER_JAR%
) ELSE (
    @IF NOT "%MVNW_REPOURL%"=="" SET DOWNLOAD_URL="%MVNW_REPOURL%/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
    @IF "%MVNW_VERBOSE%"=="true" @ECHO Downloading from: %DOWNLOAD_URL%
    @powershell -Command "&{"^
        "$webclient = new-object System.Net.WebClient;"^
        "if (-not ([string]::IsNullOrEmpty('%MVNW_USERNAME%') -and [string]::IsNullOrEmpty('%MVNW_PASSWORD%'))) {"^
        "$webclient.Credentials = new-object System.Net.NetworkCredential('%MVNW_USERNAME%', '%MVNW_PASSWORD%');"^
        "}"^
        "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; $webclient.DownloadFile('%DOWNLOAD_URL%', '%WRAPPER_JAR%')"^
        "}"
    @IF "%ERRORLEVEL%"=="0" (@IF "%MVNW_VERBOSE%"=="true" @ECHO Finished downloading %WRAPPER_JAR%) ELSE (
        @ECHO Failed to download %WRAPPER_JAR% 1>&2
        @EXIT /B 1
    )
)

@IF DEFINED JAVA_HOME (
    @SET "MAVEN_JAVA_EXE=%JAVA_HOME%\bin\java.exe"
)

@SET EXEC_DIR=%CD%
@SET MAVEN_PROJECTBASEDIR=%EXEC_DIR%

@FOR %%i IN ("%EXEC_DIR%") DO @SET MAVEN_PROJECTBASEDIR=%%~fi
@SET EXEC_DIR=%MAVEN_PROJECTBASEDIR%

@"%MAVEN_JAVA_EXE%" ^
    %JVM_CONFIG_MAVEN_PROPS% ^
    %MAVEN_OPTS% ^
    %MAVEN_DEBUG_OPTS% ^
    -classpath %WRAPPER_JAR% ^
    "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" ^
    %WRAPPER_LAUNCHER% %MAVEN_CONFIG% %*
