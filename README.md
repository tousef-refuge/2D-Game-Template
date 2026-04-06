I was making a game whose project structure genuinely gave
me irreversible AIDS, so the plan for this repo was for me
to make sure I can clone and immediately get to coding.
It's not the best template ever and honestly I wouldn't
even recommend using it but it gets the job done for me,
if for some reason you plan to clone it too then:

## How to use

- [Make sure you have gradle installed](https://docs.gradle.org/current/userguide/installation.html)
- Run the following commands (from this point on replace all 
  instances of DIR with any directory of your choice, but 
  preferably something simple like C:/)
  ```commandline
  cd DIR
  git clone https://github.com/tousef-refuge/2D-Game-Template.git
  cd 2D-Game-Template
  ```
- Go to the directory you just cloned and delete the last
  3-4 lines in .gitignore. YOU MUST DO THIS OR GIT BECOMES
  DARK AND EVIL LATER
- Go to settings.gradle and rename rootProject.name to the
  game that you're making
- Open the terminal and run the following commands
  ```commandline
  cd DIR/2D-Game-Template
  gradle wrapper
  gradle build
  ```
- Do whatever you want with the code now. Go crazy aaaahhh
- If you plan to run the code just do ```gradle run```
- As an alternative to all this you can just...click on "Use this template"
  lmao I literally discovered this like 5 minutes ago

## Features

- just look through the files I don't really feel like explaining lmao
- At the end of the day this is just a template. ~~If~~ WHEN
  there is code that doesn't seem right you can always adjust it
  to your liking. This just skips all the boring stuff