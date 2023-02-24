import Data.List

{-Begin Q2.1-} 
-- List of numbers to single number
number :: [Int] -> Int
number xs = y xs 0
  where y [] acc = acc
        y (x:xs) acc  = y xs ((acc * 10) + x)
{-End Q2.1-} 

{-Begin Q2.2-}
--Returns list of digits with unequal splits of list of pairs
splits :: [a] -> [([a], [a])]
splits []     = []
splits [x]    = []
splits (x:xs) = ([x], xs) : [(x:ls, rs) | (ls, rs) <- splits xs]

producer :: [[Int]]
producer = permutations[1,2,3,4,5,6,7,8,9]

consumer:: [[Int]] -> [([Int], [Int])]
consumer [] = []
consumer (l:ls) = splits l ++ consumer ls

possibles :: [([Int],[Int])]
possibles = consumer producer

{-End Q2.2-}

{-Begin Q2.3-}
--Returns true if the numbers are palindrome
palindrome ::[Int]-> Bool
palindrome x = x == reverse x

digits :: Int -> [Int]
digits d
 |d <=0 = []
 |otherwise =  digits (div d 10) ++ [mod d 10]

--Returns first digit
firstDigit:: [Int]-> Int
firstDigit n = head n

--Returns last digit
lastDigit:: [Int] -> Int
lastDigit n= last n

smallest :: [Int]-> [Int]->[Int]
smallest e d
  |number e < number d = e
  |otherwise = d

isAcceptable :: ([Int],[Int])-> Bool
isAcceptable(e,d)= palindrome i && firstDigit i  == 4&& lastDigit j == 3
  where i = digits(number e * number d)
        j = smallest e d

--Calls the isAcceptable function to return list of six acceptable solutions of the Teaser
acceptables :: [([Int],[Int])]
acceptables = filter isAcceptable possibles
{-End Q2.3-}