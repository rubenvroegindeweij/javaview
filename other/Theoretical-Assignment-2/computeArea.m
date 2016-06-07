% compute area of a triangle given the coordinates of the vertices
function [area] = computeArea(p1, p2, p3)
% edges
e1 = p3 - p2;
e2 = p1 - p3;
e3 = p2 - p1;
% length of edges
e1_length = norm(e1);
e2_length = norm(e2);
e3_length = norm(e3);
% apply Heron's formula
s = (e1_length + e2_length + e3_length) / 2;
area = sqrt(s*(s-e1_length)*(s-e2_length)*(s-e3_length));
end