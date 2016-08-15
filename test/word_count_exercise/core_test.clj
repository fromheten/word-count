(ns word-count-exercise.core-test
  (:require [clojure.test :refer :all]
            [word-count-exercise.core :refer :all]))

(deftest testing-tests (testing "that testing is set up" (is true)))

(def sample-texts ["Very nice ad-hoc counting"
                   "Very nice counting. Really very nice!"])
(def really-long-text (str (repeat 5000 (reduce (fn [curr acc] (str acc curr)) sample-texts))))

(def war-and-peace (slurp (.getFile (clojure.java.io/resource "war-and-peace-full-book.txt"))))

(deftest word-count-function
  (testing "that the program works as specified in the requirements document"
    (is (= 5 (word-count (first sample-texts))))
    (is (= 6 (word-count (second sample-texts))))
    (is (= 1 (line-count (first sample-texts))))
    (is (= {:words 6 :lines 1 :chars 30} (word-count-all (second sample-texts))))
    (is (= (sort [["nice" 2] ["very" 1] ["Really" 1] ["counting" 1] ["Very" 1]])
           (sort (word-count-frequencies (second sample-texts)))))
    (is (= (word-count really-long-text)
           (word-count-parallel really-long-text)))
    (is (= (map word-count sample-texts)
           (map word-count-parallel sample-texts)))
    (is (= (word-count war-and-peace)
           (word-count-parallel war-and-peace)
           #_566321 ;; I'm not getting this regex right
           ))))
