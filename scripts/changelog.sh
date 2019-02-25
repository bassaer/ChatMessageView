#!/bin/bash

function usage {

  cat <<EOF
Usage: $(basename "$0") [OPTION]...
  -v          version name
  -d          description
  -h          display help
EOF
  exit 2
}

vflg=0
dflg=0

while getopts :vdh OPT
do
    case $OPT in
        v) vflg=1
            ;;
        d) dflg=1
            ;;
        h|*) usage
            ;;
    esac
done

if [ $vflg -eq 1 ]; then
    version=$(cat CHANGELOG.md | awk '
    tolower($0) ~ /^ver.* / {
        print $2
        exit
    }')
    echo $version
    exit 0
fi

if [ $dflg -eq 1 ]; then
    desc=$(cat CHANGELOG.md | awk '
    BEGIN {
        ORS = "\\n";
    }
    tolower($0) ~ /ver.* /,/NF/ {
        if (tolower($0) ~ /^ver.*/) {
            next
        }
        if (NF==0) {
            exit
        }
        print $0
    }')
    echo $desc
    exit 0
fi
