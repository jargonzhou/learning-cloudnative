# nwdiag
* https://github.com/blockdiag/nwdiag
* fonts: http://blockdiag.com/en/nwdiag/introduction.html

> nwdiag generate network-diagram image file from spec-text file.

> nwdiag package contains three commands: nwdiag, packetdiag and rackdiag. [ref](https://groups.google.com/g/blockdiag-discuss/c/gp0_1fBbFcE)


WARN: not well-documented grammar see [parser.py](https://github.com/blockdiag/nwdiag/blob/master/src/packetdiag/parser.py)


```shell
!pip install blockdiag
!pip install funcparserlib
!pip install nwdiag
# https://github.com/tensorflow/models/issues/11040
!pip install Pillow==9.5.0


nwdiag --version
nwdiag --help
```

Sample:
```shell
nwdiag -f C:/Windows/Fonts/DejaVuSansMono.ttf -o diag/sample.png diag/sample.diag
# from IPython.display import Image
# Image(filename="diag\\sample.png") 
```

TCP Packet Format:
```shell
!packetdiag -f C:/Windows/Fonts/DejaVuSansMono.ttf -o diag/tcp.png diag/tcp.diag
<!-- from IPython.display import Image
Image(filename="diag\\tcp.png")  -->
```

rack:
```shell
!rackdiag -f C:/Windows/Fonts/DejaVuSansMono.ttf -o diag/rack.png diag/rack.diag
# from IPython.display import Image
# Image(filename="diag\\rack.png") 
```

