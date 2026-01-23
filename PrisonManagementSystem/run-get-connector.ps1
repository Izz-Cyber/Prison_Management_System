<#
PowerShell script to download the latest MySQL Connector/J (mysql-connector-java) from Maven Central
Saves the jar to the project's lib\ directory.

Usage:
  .\run-get-connector.ps1            # downloads latest release
  .\run-get-connector.ps1 -Version 8.0.33
#>

param(
    [string]$Version = ''
)

try {
    $projDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
    $libDir = Join-Path $projDir 'lib'
    if (-not (Test-Path $libDir)) { New-Item -ItemType Directory -Path $libDir | Out-Null }

    if ([string]::IsNullOrEmpty($Version)) {
        Write-Host "Fetching latest version info from Maven Central..."
        $metaUrl = 'https://repo1.maven.org/maven2/mysql/mysql-connector-java/maven-metadata.xml'
        $resp = Invoke-WebRequest -Uri $metaUrl -UseBasicParsing -ErrorAction Stop
        $xml = [xml]$resp.Content
        $ver = $xml.metadata.versioning.release
        if ([string]::IsNullOrEmpty($ver)) {
            $versions = $xml.metadata.versioning.versions.version
            $ver = $versions[$versions.Count - 1]
        }
        $Version = $ver
    }

    if ([string]::IsNullOrEmpty($Version)) { throw "Unable to determine connector version." }

    $jarName = "mysql-connector-java-$Version.jar"
    $jarUrl = "https://repo1.maven.org/maven2/mysql/mysql-connector-java/$Version/$jarName"
    $outPath = Join-Path $libDir $jarName

    Write-Host "Downloading $jarUrl to $outPath ..."
    Invoke-WebRequest -Uri $jarUrl -OutFile $outPath -UseBasicParsing -ErrorAction Stop

    if (Test-Path $outPath) {
        $size = (Get-Item $outPath).Length
        if ($size -gt 0) {
            Write-Host "Downloaded successfully: $outPath ($([math]::Round($size/1KB,2)) KB)"
            Write-Host "You can now run .\run.bat or .\run.ps1 to start the app with the JDBC driver."
            exit 0
        }
    }
    throw "Download failed or file is empty."
} catch {
    Write-Error "Error: $($_.Exception.Message)"
    exit 1
}
