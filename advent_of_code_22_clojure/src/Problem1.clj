(ns Problem1
  (:require [clojure.string :as str]))

(def input (slurp "Problem1.txt"))
(defn remove-blank-strings [col] (filter #(not (str/blank? %)) col))

(as->
  input _
  (str/split-lines _)
  (partition-by str/blank? _)
  (map remove-blank-strings _)
  (filter not-empty _)
  (map (fn [x] (map #(Integer/parseInt %) x)) _)
  (map (fn [x] (reduce + x)) _)
  (sort _)
  {
   :p1 (last _)
   :p2 (reduce + (take-last 3 _))
   }
  (println _)
  )