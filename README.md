# Slider

From arcades to pokemon, many games love to feature the "ice slider" puzzle. You start on a tile, pick a direction, and you slide in that direction until you hit a wall. Through a series of well placed moves, you might be able to reach the exit.

I thought that this puzzle ought to be implemented in 3D. Over the course of my junior and senior years of high school I have used this project to focus on learning computer graphics and the OpenGL API for the actual rendering part of the game.

Some of the features of which I am particularly proud include the proximity based wall occlusion, where the surrounding edges are hidden away when the camera gets close. Another feature that I had fun implementing are the shader effects, including the weak depth of field and the outlines.

![save](https://raw.githubusercontent.com/JosephSullivan256/Slider/master/save2018-11-03%2014-48-36.png)

## Future Plans

The first priority is to make the game actually playable rather than just a floating camera. After that, I want to make the level generation easier. As of now, there is typically only one solution, and a missplaced move ruins the entire game. Beyond this, I could also change the style to aid gameplay rather than having random textures and colors for each block.
