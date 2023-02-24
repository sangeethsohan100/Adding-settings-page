% Begin Q4.1

% helper function for prime()
divisible(X,Y) 
	:-	0 is X mod Y, !.
divisible(X,Y)
	:-	X > Y+1, divisible(X, Y+1).

% Checks if a number is prime
prime(2) 
	:-	true,!.
prime(X)
	:-	X < 2,!,false.
prime(X)
	:-	not(divisible(X, 2)).

% End Q4.1

% Begin Q4.2

% The possible range of values for bearings to be in 3 quadrants and have no dulplicate digits
possible(X,Y,Z)
	:-	between(145, 179, X),
    	between(245, 269, Y),
    	between(345, 359, Z).

% End Q4.2

% Begin Q4.3

getDigits(X,D1,D2,D3)
	:-	D3 is X mod 10, % 3rd digit
    	D2 is ((X-D3) mod 100)/10, % 2nd digit
    	D1 is ((X-(D2*10+D3)) mod 1000)/100. % 1st digit

% Checks that the length of the list is 9
nineDigits(X)
	:-	length(X,9).

% Checks that all 9 digits are present
allDigits(X)
	:-	member(1,X),
		member(2,X),
    	member(3,X),
    	member(4,X),
    	member(5,X),
    	member(6,X),
    	member(7,X),
    	member(8,X),
	    member(9,X).

% Checks that all requirements are met - no prime numbers, 9 different digits
acceptable(X,Y,Z)
	:-	getDigits(X, X1, X2, X3), % 1st number digits
    	getDigits(Y, Y1, Y2, Y3), % 2nd number digits
    	getDigits(Z, Z1, Z2, Z3), % 3rd number digits
    	D = [X1,X2,X3,Y1,Y2,Y3,Z1,Z2,Z3], % list of digits
    	allDigits(D), % checks that all 9 digits are present
    	nineDigits(D), % checks that the 3 numbers total 9 digits
    	not(prime(X)), % 1st number is not prime
    	not(prime(Y)), % 2nd number is not prime
    	not(prime(Z)). % 3rd number is not prime

% Checks that it is acceptable, and the spires are in different quadrants
trait(X,Y,Z)
	:-	possible(X,Y,Z),
    	acceptable(X,Y,Z).

% End Q4.3