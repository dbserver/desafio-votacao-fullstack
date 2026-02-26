import { Pagination } from "@heroui/pagination";
import { useParams } from "next/navigation";
import { useRouter } from "next/router";

import TRullingPaginationInfo from "@/domain/Rulling/Types/TRullingPaginationInfo";
import { Rulling } from "@/domain/Rulling/Model/Rulling";
import Items from "@/components/Rulling/feature/ListRullings/Items";

type Props = {
  paginationData: TRullingPaginationInfo;
  items: Rulling[];
  refetch?: VoidFunction;
};

export default function ListRullings({
  paginationData,
  items,
  refetch,
}: Props) {
  const { page } = useParams<{ page: string }>();
  const { push } = useRouter();

  return (
    <>
      <section className={"w-full py-2"}>
        <Items items={items} refetch={refetch} />
      </section>
      {paginationData.pages > 1 && (
        <footer className={"w-full py-2"}>
          <Pagination
            color={"primary"}
            defaultValue={parseInt(page) || 1}
            size={"sm"}
            total={paginationData.pages}
            variant={"bordered"}
            onChange={(page) => push(`/profile/running/${page}`)}
          />
        </footer>
      )}
    </>
  );
}
