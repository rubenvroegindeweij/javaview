% Geometric Modeling
% Theoretical Assignment 2
% Task 2
% Task 3 in also included

% clear previous result
clear;
% clear command window
clc;
% compute the area of the triangle using Heron's formula
% vertices of the given triangle
p1 = [1 1];
p2 = [2 2];
p3 = [-1 2];
area = computeArea(p1, p2, p3)

% compute the point with the following barycentric coordinates
alpha = [0.2 0.3 0.5];
% the result is stored in p_bc
p_with_bc_given = alpha(1)*p1 + alpha(2)*p2 + alpha(3)*p3

% compute barycentric coordinate of points
q1 = [0 1.5];
q2 = [0 0];
q1_bary = computeBarycentricCoordinates(p1, p2, p3, q1)
q2_bary = computeBarycentricCoordinates(p1, p2, p3, q2)

% compute linear polynomial function values
u_at_vertices = [1.5 3.4 -3.0];
% inside LinPoly the barycentric coordinates are calculated again
% so it can be called bafore knowing the barycenter
q1_lin_poly = LinPoly(p1, p2, p3, q1, u_at_vertices)
q2_lin_poly = LinPoly(p1, p2, p3, q2, u_at_vertices)
