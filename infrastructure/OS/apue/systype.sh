case `uname -s` in
"Linux")
	PLATFORM="linux"
	;;
"Darwin")
	PLATFORM="macos"
	;;
*)
	echo "Unknown platform" >&2
	exit 1
esac
echo $PLATFORM
exit 0