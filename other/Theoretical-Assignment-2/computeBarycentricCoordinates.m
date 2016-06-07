% This function computes barycentric coordinates of a point q
% w.r.t to given triangle (p1, p2, p3) in the 2D domain
function [q_bary] = computeBarycentricCoordinates(p1, p2, p3, q)
syms alpha1 alpha2 alpha3
eqn1 = alpha1 + alpha2 + alpha3 == 1;
eqn2 = alpha1*p1(1) + alpha2*p2(1) + alpha3*p3(1) == q(1);
eqn3 = alpha1*p1(2) + alpha2*p2(2) + alpha3*p3(2) == q(2);
[A,B] = equationsToMatrix([eqn1, eqn2, eqn3], [alpha1, alpha2, alpha3]);
q_bary = linsolve(A, B);
end