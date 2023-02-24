%Begin Q3.1

indices([],_,[]).
indices([H|T],X,[V|W])
	:-	nth0(H, X, V),
    	indices(T, X, W).
    
%End Q3.1

%Begin Q3.2

%Returns all possible solutions of a suko puzzle
possible(GRID)
	:-	length(GRID, 9), % Returns true if list has length of 9
    	indices(_,GRID,[1,2,3,4,5,6,7,8,9]).

%End Q3.2

%Begin Q3.3

% Returns sum of a list
sumOfList([], 0).
sumOfList([H|T], X)
	:-	sumOfList(T, S),
		X is H + S.

acceptable(C1,C2,C3,C4,IA,SA,IB,SB,IC,SC,GRID)
    % Checking that 4 corner square sums equal their corresponding white circles
	:-	indices([0,1,3,4],GRID,V1),
    	sumOfList(V1, C1),
    	indices([1,2,4,5],GRID,V2),
    	sumOfList(V2, C2),
    	indices([3,4,6,7],GRID,V3),
    	sumOfList(V3, C3),
    	indices([4,5,7,8],GRID,V4),
    	sumOfList(V4, C4),
    % Checking that the sum of the colored squares match the correlated outside circles
    	indices(IA,GRID,VA),
    	sumOfList(VA, SA),
    	indices(IB,GRID,VB),
    	sumOfList(VB, SB),
    	indices(IC,GRID,VC),
    	sumOfList(VC, SC).

suko(C1,C2,C3,C4,IA,SA,IB,SB,IC,SC,GRID)
	:-	possible(GRID),
    	acceptable(C1,C2,C3,C4,IA,SA,IB,SB,IC,SC,GRID).
    
%End Q3.3