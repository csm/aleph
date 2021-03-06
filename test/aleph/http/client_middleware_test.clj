(ns aleph.http.client-middleware-test
  (:require [aleph.http.client-middleware :as middleware]
    [clojure.test :as t :refer [deftest is]]))

(deftest test-empty-query-string
  (is (= "" (middleware/generate-query-string {})))
  (is (= "" (middleware/generate-query-string {} "text/plain; charset=utf-8")))
  (is (= "" (middleware/generate-query-string {} "text/html;charset=ISO-8859-1"))))

(deftest test-coerce-form-params
  (is (= "{\"foo\":\"bar\"}" (middleware/coerce-form-params {:content-type :json
                                                             :form-params {:foo :bar}})))
  (is (= "[\"^ \",\"~:foo\",\"~:bar\"]" (slurp (middleware/coerce-form-params {:content-type :transit+json
                                                                               :form-params {:foo :bar}}))))  
  (is (= "{:foo :bar}" (middleware/coerce-form-params {:content-type :edn
                                                       :form-params {:foo :bar}})))
  (is (= "foo=%3Abar" (middleware/coerce-form-params {:content-type :default
                                                      :form-params {:foo :bar}})))
  (is (= (middleware/coerce-form-params {:content-type :default
                                         :form-params {:foo :bar}})
         (middleware/coerce-form-params {:form-params {:foo :bar}}))))
