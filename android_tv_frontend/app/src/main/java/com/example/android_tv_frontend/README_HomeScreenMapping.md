# Home Screen Mapping (HTML -> Native Compose for TV)

- Header (assets/home-page-1-2.html .header):
  - Top nav pill and items -> FocusablePillText within HeaderBar()
  - Avatar/search -> simplified placeholders (can be wired to real images/icons)
  - Logo pieces -> represented by a rounded rectangle placeholder (replace with images if required)

- Highlights section (.highlights):
  - Main hero image -> HeroBanner(imagePath = figma_image_1_13.png), eager loaded

- Seguí viendo (.seguiviendo):
  - Horizontal carousel -> CarouselRow(title="Seguí viendo", PosterProgressCard items)
  - Card: poster image, progress bar, and title

- Canales de TV (.canales):
  - Horizontal carousel -> CarouselRow(title="Canales de TV", TvChannelCard items)
  - Card: left poster, play button, info texts, live pill

Focus & D-pad:
- Directional focus works across elements; visual focus via glow and selected pill.
- Carousels use LazyRow with spacing approximating the design.

Image loading:
- Coil AsyncImage with crossfade, supports placeholders and error overlay enhancements if needed.

Integration:
- HomeActivity is now the launcher activity and sets Compose content directly.
