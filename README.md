# sawitpro-asg

1. Download OAuth 2.0 Client IDs json credentials and rename as `credentials.json`
2. Place `credentials.json` inside `resources` dir
3. Download [tessedata](https://github.com/tesseract-ocr/tessdata) and replace `TESSDATA_DIRECTORY_PATH`
4. Download [chromedriver.exe](https://sites.google.com/chromium.org/driver) and replace `C:\Program Files\chromedriver.exe`
5. Download [opencv](https://sourceforge.net/projects/opencvlibrary/) and extract
6. Add VM options to run configuration `-Djava.library.path="absolute_path_to_opencv_dir\build\java\x64"`
7. Run
