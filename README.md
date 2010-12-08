# Txt2Sema4

Ever have to spell something out for someone over the phone?  Type any string into the text box on this program and click "pronounce" and this program will use the [NATO Phonetic Alphabet](http://en.wikipedia.org/wiki/NATO_phonetic_alphabet) to give you a word to say for each letter you are trying to spell.  Saves the awkward searching for words whose first letters sound like the letter you are trying to say.

For example, the program converts "foo" into foxtrot oscar oscar and shows it on screen, optionally speaking it out loud to you if your phone has text-to-speech(TTS) capability installed.


## Download/Install/Compatibility

The Android apk installer file is available under [downloads](https://github.com/jonathana/txt2sema4/downloads).  If you have allowed your phone to install out-of-market apps, you can browse to an installer link on your Android phone or click the link in a regular browser and scan the QR code with your phone to install it.  I will probably put this in the google market eventually.

The application should be compatible with Android versions 1.6 and higher.  It does not have any special requirements.  It is Samsung Galaxy Tab-compatible, and runs on that, my Nexus One, and a MyTouch 3G.


## Build Instructions

If you really want to build it yourself, the necessary artifacts to build it yourself with the Android Development Kit and Eclipse should be checked in.

Feel free to contact me via github with any questions, file bugs there, or fork, update, and send me pull requests if you get motivated to improve on this.

## Future Ideas

Add support for other phonetic alphabets than the NATO one.  I suspect the most flexible way to support that is to move the letter->word data structure into Android's SQLite data storage, and then the user can choose from multiple, named sets of phonetic alphabets.  Eventually it would be nice to allow creation, editing, and deletion of alphabets on the device.

See what i18n/l15n options the TTS engine might offer.

Have fun,

Jonathan
