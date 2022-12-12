(ns Problem3
  (:require
    [clojure.string :as str]
    [clojure.set :as sets]
    ))

(def input (str/split-lines (slurp "Problem3.txt")))
(defn split-line-in-half [line]
  [
   ;subs is substring because sometimes life is sad
   (subs line 0 (/ (count line) 2))
   (subs line (/ (count line) 2))
   ])

;Nonsense character to score mapping
(defn priority [line] (map #(if (>= (int %) 97) (- (int %) 96) (+ 27 (- (int %) 65))) line))
(as->
  input _
  ;(map split-line-in-half _) p1
  ;partition to 3 is part 2
  (partition 3 _)
  (map (fn [line] (map set line)) _)
  (map (fn [line] (reduce sets/intersection line)) _)
  (map priority _)
  (flatten _)
  (reduce + 0 _)
  (println _)
  )

