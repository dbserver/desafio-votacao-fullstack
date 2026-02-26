import { Rulling } from "@/domain/Rulling/Model/Rulling";
import Item from "@/components/Rulling/feature/ListRullings/Item";

type Props = {
  items: Rulling[];
  refetch?: VoidFunction;
};
export default function Items({ items, refetch }: Props) {
  return (
    <>
      <section className="grid grid-cols-4 gap-2">
        {items.map((item) => (
          <>
            <Item key={item.id} item={item} refetch={refetch} />
          </>
        ))}
      </section>
    </>
  );
}
