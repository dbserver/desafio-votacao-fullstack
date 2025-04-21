export const formatMaskCPF = (value: string): string => {
  const getNumbers = value.replace(/\D/g, '');

  return getNumbers
    .replace(/^(\d{3})(\d)/, '$1.$2')
    .replace(/^(\d{3})\.(\d{3})(\d)/, '$1.$2.$3')
    .replace(/(\d{3})(\d)/, '$1-$2')
    .substring(0, 14);
};

export const removeMask = (value: string) => {
  return value ? value?.replace(/\D/g, '') : '';
};
