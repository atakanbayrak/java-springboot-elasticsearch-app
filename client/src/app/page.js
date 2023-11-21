"use client";
import { useState, useEffect } from "react";

const SearchBox = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [suggestions, setSuggestions] = useState([]);

  useEffect(() => {
    const fetchSuggestions = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/v1/items/suggestionsQuery/${searchTerm}`);
        const data = await response.json();
        console.log(data);
        setSuggestions(data);
      } catch (error) {
        console.error("Error fetching suggestions:", error);
      }
    };

    if (searchTerm.length > 0) {
      fetchSuggestions();
    } else {
      setSuggestions([]);
    }
  }, [searchTerm]);

  return (
    <div className="relative mt-10 flex flex-col items-center">
      <input type="text" className="border border-gray-300 p-2 rounded-md w-1/2" placeholder="Ara..." value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} />
      {suggestions && (
        <ul className="flex flex-col justify-center left-0 right-0 mt-2 bg-white border border-gray-300 rounded-md shadow-md w-1/2">
          {suggestions.map((suggestion, index) => (
            <li key={index} className="p-2 cursor-pointer flex hover:bg-gray-100 text-black">
              {suggestion}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default SearchBox;
