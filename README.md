## Synopsis

ComplexOverlayTiles is an example of how more detailed tiles can be generated for overlays on Google Maps for Android V2. With this, anything that can be drawn to a canvas can be drawn to tiles and overlaid on the map.

## Motivation

This came out of necessity because of the inherent limitations of Polyline and GroundOverlay. Polylines in V2 are individually drawn on the UI thread, so doing complicated route overlays is not possible without seriously lagging the UI. GroundOverlays can be prepackaged from Mapnik or similar, but don't handle zooming well. 

## Installation

This app has a few dependencies. The first is of course Google Play Services lib, which is directly available from Google. The second is map-utils-library, as this project utilizes it's spherical mercator projection extension for tile generation. It is available [here][website] (more info [here][details])

[website]: https://github.com/googlemaps/android-maps-utils
[details]: http://googlemaps.github.io/android-maps-utils/

## License

[Apache License 2.0][license]
[license]: http://www.apache.org/licenses/