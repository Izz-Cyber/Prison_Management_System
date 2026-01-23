#!/usr/bin/env pwsh
Write-Host "Running PrisonManagementSystem..."
$projDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$out = Join-Path $projDir 'out'
$lib = Join-Path $projDir 'lib'
$jar = Get-ChildItem -Path $lib -Filter 'mysql-connector-java-*.jar' -ErrorAction SilentlyContinue | Select-Object -First 1
if (-not $jar) { $jar = Get-ChildItem -Path (Join-Path $projDir 'src') -Filter 'mysql-connector-j-*.jar' -ErrorAction SilentlyContinue | Select-Object -First 1 }
if (-not $jar) { $jar = Get-ChildItem -Path (Join-Path $projDir 'src') -Filter 'mysql-connector-java-*.jar' -ErrorAction SilentlyContinue | Select-Object -First 1 }
if ($jar) {
    Write-Host "Using JDBC jar: $($jar.FullName)"
    & cmd /c "java -cp `"$out;$($jar.FullName)`" Main.App"
} else {
    Write-Host "MySQL connector jar not found in $lib or src. Running demo mode (MockDBManager)."
    & cmd /c "java -cp `"$out`" Main.App"
}
