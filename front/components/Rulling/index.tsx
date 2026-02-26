"use client";
import { useEffect } from "react";
import { useParams } from "next/navigation";
import { Spinner } from "@heroui/spinner";
import { TbError404 } from "react-icons/tb";

import NewRunning from "@/components/Rulling/feature/NewRunning";
import ListRullings from "@/components/Rulling/feature/ListRullings";
import useApiHookPaginatedRullingsInfo from "@/domain/Rulling/Api/useApiHookPaginatedRullingsInfo";
import useApiHookRullings from "@/domain/Rulling/Api/useApiHookRullings";

export default function Rulling() {
  const params = useParams<{ page: string }>();
  const { fetch: fetchPaginations, response: paginationData } =
    useApiHookPaginatedRullingsInfo();
  const { fetch, isLoading, response } = useApiHookRullings();

  useEffect(() => {
    fetchPaginations(null);
  }, []);

  useEffect(() => {
    if (params && params.page) fetch(parseInt(params.page));
  }, [params]);

  return (
    <>
      <header className={"w-full py-4 flex justify-end items-end"}>
        <NewRunning
          refetch={() => {
            fetch(parseInt(params.page));
          }}
        />
      </header>
      <section className={"w-full"}>
        {isLoading && (
          <div className={"w-full h-[400px] flex justify-center items-center"}>
            <Spinner size={"lg"} />
          </div>
        )}

        {(paginationData && !isLoading && response && response.length && (
          <ListRullings
            items={response}
            paginationData={paginationData}
            refetch={() => {
              fetch(parseInt(params.page));
            }}
          />
        )) || (
          <>
            <section
              className={"w-full h-[400px] flex justify-center items-center"}
            >
              <TbError404 size={88} />
            </section>
          </>
        )}
      </section>
    </>
  );
}
