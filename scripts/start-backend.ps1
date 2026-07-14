param(
    [string]$TesseractHome = 'D:\TESSERACT-OCR',
    [string]$JavaHome = 'C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot'
)

$ErrorActionPreference = 'Stop'
$executable = Join-Path $TesseractHome 'tesseract.exe'
$tessdata = Join-Path $TesseractHome 'tessdata'

if (-not (Test-Path -LiteralPath $executable)) {
    throw "Tesseract was not found at $executable"
}

$languages = & $executable --list-langs 2>&1 | Out-String
foreach ($language in @('eng', 'fra', 'ara')) {
    if ($languages -notmatch "(?m)^$language$") {
        throw "Required Tesseract language '$language' is missing from $tessdata"
    }
}

if (-not (Test-Path -LiteralPath (Join-Path $JavaHome 'bin\java.exe'))) {
    throw "Java 21 was not found at $JavaHome"
}

$env:JAVA_HOME = $JavaHome
$env:Path = "$TesseractHome;$env:Path"
$env:TESSDATA_PREFIX = $tessdata

Write-Host 'Starting Curriva with Tesseract OCR languages: eng, fra, ara'
mvn -pl backend spring-boot:run
