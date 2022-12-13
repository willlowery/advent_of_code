(ns Problem4
  (:require
    [clojure.string :as str]
    [clojure.set :as sets]
    )
  )

(def input (str/split-lines (slurp "Problem4.txt")))
(def line-regex #"(\d+)-(\d+),(\d+)-(\d+)")
;(println (re-matches line-regex "21-81,20-96"))
;
(defn line-to-ranges
  "Converts the lines (\\d+)-(\\d+),(\\d+)-(\\d+) into to ranges [[a b] [c d]]"
  [line]
  (let
    [apart (re-matches line-regex line)]
    [
     (range
       (Integer/parseInt (nth apart 1))
       (+ 1 (Integer/parseInt (nth apart 2)))
       )
     (range
       (Integer/parseInt (nth apart 3))
       (+ 1 (Integer/parseInt (nth apart 4)))
       )
     ]
    ))

(defn detect-full-overlap
  [ranges]
  (let
    [
     intersection-size (count (reduce sets/intersection ranges))
     first-size (count (first ranges))
     second-size (count (second ranges))
     ]
    (if (or
          (= intersection-size first-size)
          (= intersection-size second-size))
      1 0)))

(defn detect-partial-overlap
  [ranges]
  (if (> (count (reduce sets/intersection ranges)) 0)
    1 0))

(as->
  input _
  (map line-to-ranges _)
  (map (fn [ranges] (map set ranges)) _)
  {
   :p1 (reduce + 0 (map detect-full-overlap _))
   :p2 (reduce + 0 (map detect-partial-overlap _))
   }
  (println _))

