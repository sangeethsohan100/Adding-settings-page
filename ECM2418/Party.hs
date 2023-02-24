import Data.List

{-Begin Q1.1-}
digits :: Int -> [Int]
digits n
 |n <=0 = []
 |otherwise =  digits (div n 10) ++ [mod n 10]
{-End Q1.1-}
 
{-Begin Q1.2-} 
removeDuplicates :: Eq a => [a] -> [a]
removeDuplicates [] = []
removeDuplicates (x:xs)
  |elem x xs = removeDuplicates xs
  |otherwise = x : removeDuplicates xs

--Checks that there are no duplicate digits
noDuplicateDigits :: Int -> Bool 
noDuplicateDigits
  i = length l == length(removeDuplicates l) && not (elem 0 l)
  where l = digits i

factor :: Int -> Int -> Bool
factor i j = (mod j i) == 0

--Returns true if the last two digits are multiple of the number represented by first two digits and there are no duplicate digits
isPar :: Int -> Bool
isPar i = factor firstTwo lastTwo
  where
  firstTwo= div i 100
  lastTwo= i
  
producer = filter noDuplicateDigits [1000..9999]

consumer :: [Int] -> [Int]
consumer = filter isPar

pars :: [Int]
pars= consumer producer
{-End Q1.2-}

{-Begin Q1.3-}
--Returns true if pair of integers which are pars is a PARTY
isParty :: (Int,Int) -> Bool
isParty(i,j)= isPar i && isPar j && noDuplicate && factor k i && factor k j
  where 
  digitAdd= digits (i) ++ digits(j)
  noDuplicate = length (removeDuplicates digitAdd) == 8
  k = head([1..9] \\ digitAdd)
  
partyChecker :: Int -> [Int] -> [(Int,Int)]
partyChecker e [] = []
partyChecker e (x:xs)
  |isParty (e,x)=(e,x) : partyChecker e xs
  |otherwise = partyChecker e xs

-- Calls itself and the partyChecker function to check for partys
partyProducer :: [Int] -> [Int] -> [(Int, Int)]
partyProducer [] y = []
partyProducer (x: xs) y = partyChecker x y ++ partyProducer xs y

-- Gets the list of all 14 pars
partys:: [(Int, Int)]
partys = partyProducer pars pars 
{-End Q1.3-}