// Assignment 6: Arrays, Objects, and Core Methods
// This script demonstrates arrays of objects with forEach(), filter(), and map().

// ─── 1. Create an array of product objects ──────────────────────────
const products = [
    { name: "Laptop", price: 800 },
    { name: "Mouse", price: 25 },
    { name: "Keyboard", price: 45 }
];

// ─── 2. forEach() - Print each product ──────────────────────────────
console.log("-- All Products (forEach) --");

products.forEach((product) => {
    console.log("Item: " + product.name + " - Price: $" + product.price);
});

// ─── 3. filter() - Find items under $50 ─────────────────────────────
console.log("\n-- Filtered Products (Price < $50) --");

const affordableProducts = products.filter((product) => product.price < 50);
console.log(affordableProducts);

// ─── 4. map() - Create formatted name-price strings ─────────────────
console.log("\n-- Mapped Product Tags (map) --");

const productTags = products.map((product) => {
    return product.name.toUpperCase() + " costs $" + product.price;
});
console.log(productTags);
