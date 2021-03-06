# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project (mostly) adheres to [Semantic Versioning](http://semver.org/).

## [0.1.1] - 2017-05-12
### Added
* #111/#116: Debug option for importing Steam library

### Changed
* #108: Selectable checkboxes for editing and deleting games
* #117: NewGameWindow uses star images for ratings

## [0.1.0] - 2017-04-13
First functional release, featuring the ability to edit entries in the gamelist,
and the option to save or load from a .csv file.

### Added
* #61: Debug menu for printing gameList and FileIO
* #62/#79: Game list displayed on program home screen
* #65: Dropdown list of systems loaded from `system_list.csv`
* #73: 'New' menu button clears game
* #83/#88: Save and load gamelist from file

### Changed
* #67: Drop BlueJ support in favor of Eclipse
* #99: System selection changed from dropdown to an auto-completing textbox

### Fixed
* #57: Game hours no longer always equals 0
* #75: Gamelist sorted alphabetically

## 0.0.1 - 2017-03-06
Initial non-functional alpha. Only provides a basic prototype of user interface.
Little to no actual functionality.

### Added
* #13: Automatic build testing with Travis CI
* #28: Button for adding new games
* #32/#34: Fillable form to manually add new games
* #38: Add Game and GameList classes
* #47: "About" menu button to credit project resources and contributors
* #48: "View on GitHub" menu shortcut to GitHub repository
* #51: File reader to import/export tab separated text files into an ArrayList

### Changed
* #39/#42: Sort project into packages
* #50: Separate MenuBar into its own file

[0.1.1]: https://github.com/Stevoisiak/Virtual-Game-Shelf/compare/v0.1.0...v0.1.1
[0.1.0]: https://github.com/Stevoisiak/Virtual-Game-Shelf/compare/v0.0.1...v0.1.0
[0.0.1]: https://github.com/Stevoisiak/Virtual-Game-Shelf/compare/cc75417...v0.0.1
