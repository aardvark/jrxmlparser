(ns jrxmlparser.core-test
  (:require [clojure.test :refer :all]
            [jrxmlparser.core :refer :all]
            [jrxmlparser.parser :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 1 1))))

(deftest branchs
  (testing "Get-branch on basic jrxml styles should return 3 style"
    (is (= (count (get-branchs "resources/MasterReport.jrxml" :style)) 3))))

(deftest check-usage
  (testing "Usage should return correct map list for usages inside"
    (is (= (find-usages "resources/MasterReport.jrxml" :variable)
           {"ProductTotalPrice", "\"Total order value: \" + $V{ProductTotalPrice}" }))))
