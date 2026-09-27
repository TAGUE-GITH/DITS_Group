const address = "Rue des Noyers, 77000 Livry-sur-Seine, Île-de-France";

export const company = {
  name: "DITS Group",
  email: "contact@ditsgroup.com",
  phone: "+33 1 00 00 00 00",
  address,
  mapsUrl: `https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(address)}`,
  mapsEmbedUrl: `https://www.google.com/maps?q=${encodeURIComponent(address)}&output=embed`,
};