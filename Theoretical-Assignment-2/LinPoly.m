% compute the value of linear polynomial at certain points Q
function [value] = LinPoly(p1, p2, p3, q, u)
	barycenter = computeBarycentricCoordinates(p1, p2, p3, q);
	value = barycenter(1)*u(1) + barycenter(2)*u(2) + barycenter(3)*u(3);
end