@echo off
chcp 65001 >nul
echo ========================================
echo    毕业记录管理系统 - 控制台演示
echo    Activity 1: Course & Student Demo
echo ========================================
echo.

echo [1/4] 检查Java环境...
java -version >nul 2>&1
if errorlevel 1 (
    echo 错误: 未找到Java环境，请先安装JDK
    pause
    exit /b 1
)
echo Java环境检查通过
echo.

echo [2/4] 设置本地Maven环境...
set "MAVEN_HOME=%~dp0apache-maven-3.9.11"
set "PATH=%MAVEN_HOME%\bin;%PATH%"
echo Maven环境设置完成
echo.

echo [3/4] 编译项目...
call mvn compile -q
if errorlevel 1 (
    echo 编译失败！
    pause
    exit /b 1
)
echo 编译成功
echo.

echo [4/4] 运行Activity 1控制台演示...
echo.
java -cp "target/classes" com.graduation.GraduationRecords
echo.
echo ========================================
echo 演示完成！
echo.
echo 如需启动Web应用，请运行: start.bat
echo 按任意键退出...
echo ========================================
pause >nul
