# Evolutionary Computations Course Tasks

This is a visualization of work of an evolutionary algorithm.

![Evolution](/animation.gif)

Implemented algorithm searches for a global extremum of a function. The following function is used as an example:
```math
f(x) = cos(3x - 15) / |x| , x in (-10, -0.3)U(0.3, 10)
       0                  , x in [-0.3, 0.3]
```

## Running
To run a visualization do:

    $ lein run -m evolution.plot

You can change parameters of algorithm in ``src/evolution/core.clj`` file.
