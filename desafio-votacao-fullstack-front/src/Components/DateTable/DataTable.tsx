import { Table } from "react-bootstrap";

export interface Column<T> {
  header: string;
  accessor: keyof T;
}

interface DataTableProps<T> {
  data: T[];
  columns: Column<T>[];
}

const DataTable = <T extends { id: number }>({ data, columns }: DataTableProps<T>) => {
  return (
    <Table striped bordered hover>
      <thead>
        <tr>
          {columns.map((column) => (
            <th key={String(column.accessor)}>{column.header}</th>
          ))}
        </tr>
      </thead>
      <tbody>
        {data.map((item) => (
          <tr key={item.id}>
            {columns.map((column) => (
              <td key={String(column.accessor)}>
                {String(item[column.accessor])}
              </td>
            ))}
          </tr>
        ))}
      </tbody>
    </Table>
  );
};

export default DataTable;