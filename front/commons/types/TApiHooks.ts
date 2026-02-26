type TApiHooks<T, F> = {
  isLoading: boolean;
  response?: T;
  error?: Error;
  fetch: (data: F) => void | VoidFunction;
};
