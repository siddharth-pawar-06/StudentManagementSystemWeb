@echo off
echo =======================================================
echo   Push Student Management System to GitHub
echo =======================================================
echo.
set /p GH_USER="Enter your GitHub Username: "
if "%GH_USER%"=="" (
    echo Error: GitHub username cannot be empty.
    pause
    exit /b 1
)

echo.
echo Setting remote origin to: https://github.com/%GH_USER%/StudentManagementSystemWeb.git
git remote remove origin 2>nul
git remote add origin https://github.com/%GH_USER%/StudentManagementSystemWeb.git
git branch -M main

echo.
echo Pushing code to GitHub...
git push -u origin main

if %ERRORLEVEL% EQU 0 (
    echo.
    echo =======================================================
    echo   SUCCESS! Your project is now live on GitHub:
    echo   https://github.com/%GH_USER%/StudentManagementSystemWeb
    echo =======================================================
) else (
    echo.
    echo If push failed, make sure you created the empty repository
    echo on https://github.com/new with name "StudentManagementSystemWeb"
)
echo.
pause
