len :: [Int] -> Int
len[]
    =0
len(x:xs)
    =1 + len xs

main = putStrLn(show(len[1,2,3,,4,5]))