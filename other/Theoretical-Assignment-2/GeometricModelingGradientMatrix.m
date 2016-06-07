clc;

P1 = [0 0 0]';
P2 = [1 1 0]';
P3 = [0 1 1]';

P1 = [-0.523035 0.474694 0.436263]';
P2 = [0.528191 0.492968 0.448928]';
P3 = [-0.714874 1.3084 -0.42234]';

%http://math.stackexchange.com/questions/305642/how-to-find-surface-normal-of-a-triangle
V = P2 - P1;
W = P3 - P1;

Nx = (V(2)*W(3)) - (V(3)*W(2));
Ny = (V(3)*W(1)) - (V(1)*W(3));
Nz = (V(1)*W(2)) - (V(2)*W(1));

N = [Nx Ny Nz];

N = N/(norm(N));

E1 = P3 - P2;
E2 = P1 - P3;
E3 = P2 - P1;

% Heron's formula
%a = norm(E1);
%b = norm(E2);
%c = norm(E3);
%s = (a + b + c)/2;
%Area = sqrt(s*(s-a)*(s-b)*(s-c));

Area = norm(cross(P3-P1, P2-P1))/2;

Triangle = [P1 P2 P3]
GradientMatrix = [cross(N, E1)' cross(N, E2)' cross(N, E3)']/(2*Area)