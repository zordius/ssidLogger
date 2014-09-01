#!/bin/sh
test -d ~/node_modules/svg2png || npm install svg2png
node -e "require('svg2png')('logo.svg', 'SSIDLogger/ic_launcher-web.png', 1.6, function(err) {err ? console.warn(err) : console.log('512x512 Done!');});"
node -e "require('svg2png')('logo.svg', 'SSIDLogger/res/drawable-xxhdpi/ic_launcher.png', 0.45, function(err) {err ? console.warn(err) : console.log('144x144 Done!');});"
node -e "require('svg2png')('logo.svg', 'SSIDLogger/res/drawable-xhdpi/ic_launcher.png', 0.3, function(err) {err ? console.warn(err) : console.log('96x96 Done!');});"
node -e "require('svg2png')('logo.svg', 'SSIDLogger/res/drawable-hdpi/ic_launcher.png', 0.225, function(err) {err ? console.warn(err) : console.log('72x72 Done!');});"
node -e "require('svg2png')('logo.svg', 'SSIDLogger/res/drawable-mdpi/ic_launcher.png', 0.15, function(err) {err ? console.warn(err) : console.log('48x48 Done!');});"
