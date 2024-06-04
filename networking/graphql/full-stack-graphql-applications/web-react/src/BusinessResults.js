import { starredVar } from ".";

function BusinessResults(props) {
  const { businesses } = props;
  // ========================================================
  // use reactive varibale
  // ========================================================
  const starredItems = starredVar();

  return (
    <div>
      <h2>Results</h2>
      <table>
        <thead>
          <tr>
            <th></th>
            <th>Name</th>
            <th>Address</th>
            <th>Category</th>
          </tr>
        </thead>
        <tbody>
          {businesses.map((b, i) => (
            <tr key={i}>
              <td>
                {b.isStarred
                  ? <button onClick={() => {
                    let starredItems2 = starredItems.filter((e) => e !== b.businessId);
                    starredVar([...starredItems2])
                  }}>Star-</button>
                  : <button onClick={() => starredVar([...starredItems, b.businessId])}>Star+</button>
                }
              </td>
              <td style={b.isStarred ? { fontWeight: "bold" } : null}>{b.name}</td>
              <td>{b.address}</td>
              <td>{b.categories.reduce((acc, c, i) => acc + (i === 0 ? " " : ", ") + c.name, "")}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default BusinessResults;