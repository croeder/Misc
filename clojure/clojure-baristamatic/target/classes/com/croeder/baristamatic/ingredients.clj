(ns com.croeder.baristamatic.ingredients)
(defstruct item :name :cost :quantity)
(defn ingredients [] 
  { 
   :coffee        (struct item "Coffee" 0.75  10)
   :decaf         (struct item "Decaf Coffee" 0.75 10)
   :sugar         (struct item "Sugar" 0.25 10)
   :cream         (struct item "Cream" 0.25 10)
   :steamed_milk  (struct item "Steamed Milk" 0.35 10)
   :foamed_milk   (struct item "Foamed Milk" 0.35 10)
   :espresso      (struct item "Espresso" 1.10 10)
   :cocoa         (struct item "Cocoa" 0.90 10)
   :whipped_cream (struct item "Whipped Cream" 1.00 10)})
(defn ingredients-low []
  "for testing, coffee and cappuccino might not go mroe than ocne"
  { 
   :coffee        (struct item "Coffee" 0.75  1)
   :decaf         (struct item "Decaf Coffee" 0.75 10)
   :sugar         (struct item "Sugar" 0.25 10)
   :cream         (struct item "Cream" 0.25 10)
   :steamed_milk  (struct item "Steamed Milk" 0.35 10)
   :foamed_milk   (struct item "Foamed Milk" 0.35 10)
   :espresso      (struct item "Espresso" 1.10 10)
   :cocoa         (struct item "Cocoa" 0.90 10)
   :whipped_cream (struct item "Whipped Cream" 1.00 1)})

(defn have-ingredient-quantity [ingredient-key quantity ingredients]
  (> quantity ((ingredients ingredient-key) :quantity)))

(defn consume-ingredient-quantity
  ""
  [ingredient quantity]
  (if (<= quantity (ingredient :quantity))
    ;; merge lets you over-write too
    (merge ingredient {:quantity (- (ingredient :quantity) quantity)})))

(defn make-drink
  "This function reduces the inventory for each ingredient
   by the amounts in the given recipe. It returns a new
   inventory list.
   drink-recipe is a recipe struct.
   inventory is a list of ingredients structs."
  [drink-recipe inventory]
  (for [recipe-ingredient (keys drink-recipe )]
        (if (contains? (inventory recipe-ingredient) :quantity)
          (merge (inventory recipe-ingredient)
                 {:quantity (- ((inventory recipe-ingredient) :quantity)
                               (drink-recipe recipe-ingredient))
                  })
          )))

(defn display-inventory-item [item]
  (str (:name item) "  " (:quantity item) " $" (:cost item) " ea." ))

(defn display-inventory [inventory]
  (map (fn [x]
         (do
           (display-inventory-item  (inventory x))
           ))
       (keys inventory)))


