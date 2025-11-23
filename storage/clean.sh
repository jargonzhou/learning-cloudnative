echo "Delete DataEngineering"
find . -depth -name '.bsp' -type d -print -exec rm -r {} + 
find . -depth -name '.scala-build' -type d -print -exec rm -r {} + 
find . -depth -name '.metals' -type d -print -exec rm -r {} + 
find . -depth -name '.ammonite' -type d -print -exec rm -r {} + 
find . -name '*.class' -type f -delete
