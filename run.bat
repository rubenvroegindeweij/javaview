@echo off
set jv_jar=jars/javaview.jar;jars/jvx.jar;jars/vgpapp.jar;jars\Jama-1.0.3.jar;.
start javaw -cp %jv_jar% -Djava.library.path="dll" -Xmx1024m javaview model="models/meshWithScalarFieldAndGradient0.jvx" codebase=. archive.dev=show %*