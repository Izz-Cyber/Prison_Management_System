Automatic MySQL Connector/J downloader

This project includes a helper PowerShell script to download MySQL Connector/J into the `lib\` folder.

Script: `run-get-connector.ps1`

Usage:
- From PowerShell, run:
```powershell
.\run-get-connector.ps1           # download latest
.\run-get-connector.ps1 -Version 8.0.33  # download specific version
```

After the jar is downloaded to `lib\`, run the project using `run.bat` or `run.ps1`.

Notes:
- The script fetches metadata from Maven Central and downloads the jar directly.
- It requires internet access and PowerShell.
- If you prefer not to download automatically, manually place the jar in `lib\`.
