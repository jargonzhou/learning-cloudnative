// import logo from './logo.svg';
import './App.css';
import BusinessResults from './BusinessResults'
import { useState } from 'react'
import { gql, useQuery } from '@apollo/client';

// ========================================================
// GraphQL fragment
// @client: local-only field
// ========================================================
const BusinessDetailsFragment = gql`
fragment businessDetails on Business {
  businessId
  name
  address
  categories {
    name
  }
  isStarred @client
}
`
// ========================================================
// GraphQL query
// ========================================================
const GetBusinessQuery = gql`
query BusinessesByCategory($selectedCategory: String!) {
  businesses(
    where: {categories_SOME: {name_CONTAINS: $selectedCategory}}
  ) {
    ...businessDetails
  }
}

${BusinessDetailsFragment}
`

function App() {
  // ========================================================
  // useState Hook
  // ========================================================
  const [selectedCategory, setSelectedCategory] = useState("")

  // ========================================================
  // useQuery Hook
  // ========================================================
  const { loading, error, data, refetch } = useQuery(GetBusinessQuery,
    { variables: { selectedCategory } }
  )
  if (error) return <p>Error: {JSON.stringify(error)}</p>
  if (loading) return <p>Loading...</p>

  return (
    <div>
      <h1>Business Search</h1>
      <form >
        <label>
          Select Business Category:
          <select name='selectedCategory' value={selectedCategory} onChange={(event) => setSelectedCategory(event.target.value)}>
            <option value="">All</option>
            <option value="Library">Library</option>
            <option value="Restaurant">Restaurant</option>
            <option value="Car Wash">Car Wash</option>
          </select>
        </label>
        {/* <input type="submit" value="Submit" /> */}
        <input type='button' value='Refresh' onClick={() => refetch()} />
      </form>

      <BusinessResults businesses={data.businesses} />
    </div>
  );
}

export default App;
