(ns Problem2
  (:require [clojure.string :as str]))

(defn parseLine [line]
  {
   :first
   (cond
     (str/includes? line "A") :a
     (str/includes? line "B") :b
     (str/includes? line "C") :c
     ),
   :second
   (cond
     (str/includes? line "X") :x
     (str/includes? line "Y") :y
     (str/includes? line "Z") :z
     ),
   })

(defn to-scoreable-p1 [line]
  (let
    [
     mapping
     {
      :a :rock :b :paper :c :scissor
      :x :rock :y :paper :z :scissor
      }
     ]
    (assoc line
      :first ((:first line) mapping)
      :second ((:second line) mapping)
      ))
  )

(defn to-scoreable-p2 [line]
  (let
    [mapping
     {
      :a       :rock :b :paper :c :scissor
      :x       :lose :y :tie :z :win
      :rock    {:lose :scissor :tie :rock :win :paper}
      :paper   {:lose :rock :tie :paper :win :scissor}
      :scissor {:lose :paper :tie :scissor :win :rock}
      }]
    (assoc line
      :first ((:first line) mapping)
      :second (((:second line) mapping) (((:first line) mapping) mapping))
      )
    ))

(defn score [line]
  (let
    [scores
     {
      :rock    {:rock 4 :paper 8 :scissor 3}
      :paper   {:rock 1 :paper 5 :scissor 9}
      :scissor {:rock 7 :paper 2 :scissor 6}
      }]

    ((:second line)
     ((:first line) scores))))

(def input (slurp "Problem2.txt"))

(as->
  input _
  (str/split-lines _)
  (map parseLine _)
  (map to-scoreable-p1 _)
  (map score _)
  (reduce + _)
  (println _)
  )

(as->
  input _
  (str/split-lines _)
  (map parseLine _)
  (map to-scoreable-p2 _)
  (map score _)
  (reduce + _)
  (println _)
  )



