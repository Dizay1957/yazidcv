# Tesseract OCR setup on Windows

Curriva uses Apache Tika, which calls the native `tesseract.exe` program for scanned PDF and image CVs. English, French, and Arabic language data are required. On this workstation the verified installation is `D:\TESSERACT-OCR`.

For the current PowerShell session, start Curriva with:

```powershell
$env:Path='D:\TESSERACT-OCR;' + $env:Path
$env:TESSDATA_PREFIX='D:\TESSERACT-OCR\tessdata'
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot'
mvn -pl backend spring-boot:run
```

Apache Tika 3 discovers Tesseract from the backend process `Path`, so set these variables in the same PowerShell window before starting Maven.

## Recommended installation

1. Download the current 64-bit Windows installer from the maintained UB Mannheim Tesseract distribution:
   `https://github.com/UB-Mannheim/tesseract/wiki`
2. Run the installer as administrator.
3. Keep the default directory: `C:\Program Files\Tesseract-OCR`.
4. In **Additional language data**, select English, French, and Arabic. If the installer does not offer them, download `eng.traineddata`, `fra.traineddata`, and `ara.traineddata` from `https://github.com/tesseract-ocr/tessdata_fast` and place them in `C:\Program Files\Tesseract-OCR\tessdata`.
5. Open Windows **Edit the system environment variables** → **Environment Variables**.
6. Add `C:\Program Files\Tesseract-OCR` to the system `Path`.
7. Add a system variable named `TESSDATA_PREFIX` with value `C:\Program Files\Tesseract-OCR\tessdata`.
8. Close and reopen PowerShell and your IDE so they receive the new environment.

Verify:

```powershell
tesseract --version
tesseract --list-langs
```

The language list must include `eng`, `fra`, and `ara`. Then restart the Curriva backend and upload a scanned CV. Docker users do not need a host installation: the backend Docker image already installs all three packs.

If `tesseract` is still not recognized, run it once by full path:

```powershell
& 'C:\Program Files\Tesseract-OCR\tesseract.exe' --list-langs
```

If that succeeds, the remaining issue is the Windows `Path`; restart the terminal or computer. Never download trained-data files from unknown mirrors.
