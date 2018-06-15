# anki-pi-image
Raspberry Pi image for controlling anki overdrive vehicles

## Prerequisites

* Raspberry Pi 3 or compatible
* microSD card >= 1GB
* Ethernet cable

## Usage

* Clone or download this repository to the root of your SD card
* Connect your computer to the Raspberry's ethernet port
* Set your computer's IP address to 10.200.100.21
* Set up your Anki SDK client to connect to the Raspberry at 10.200.100.13

## Root access
* You can log in to the Raspberry via SSH with user "root" and password "overdrive"
* Optionally, you can change the Raspberry's network settings with `setup-interfaces`
