param(
    [string]$TesseractHome = 'D:\TESSERACT-OCR',
    [string]$JavaHome = 'C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot',
    [switch]$ValidateOnly
)

$ErrorActionPreference = 'Stop'
$executable = Join-Path $TesseractHome 'tesseract.exe'
$tessdata = Join-Path $TesseractHome 'tessdata'

if (-not (Test-Path -LiteralPath $executable)) {
    throw "Tesseract was not found at $executable"
}

$languages = @(& $executable --list-langs 2>&1) | ForEach-Object { $_.ToString().Trim() }
foreach ($language in @('eng', 'fra', 'ara')) {
    if ($language -notin $languages) {
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
if ($ValidateOnly) {
    Write-Host 'Tesseract and Java validation passed.'
    exit 0
}
mvn -pl backend spring-boot:run
