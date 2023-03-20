/** @type {import('@docusaurus/types').Config} */
const config = {
  title: 'sbt-github',
  tagline: 'SBT plugin to read several settings from Github',
  favicon: 'img/favicon.ico',

  url: 'https://alejandrohdezma.github.io',
  baseUrl: '/sbt-github/',

  organizationName: 'alejandrohdezma',
  projectName: 'sbt-github',

  onBrokenLinks: 'throw',
  onBrokenMarkdownLinks: 'warn',

  i18n: {
    defaultLocale: 'en',
    locales: ['en'],
  },

  presets: [
    [
      'classic',
      /** @type {import('@docusaurus/preset-classic').Options} */
      ({
        docs: {
          path: 'target/mdoc/',
          editUrl: params => 'https://github.com/alejandrohdezma/sbt-github/edit/main/site/docs/' + params.docPath,
        },
        theme: {
          customCss: require.resolve('./src/css/custom.css'),
        },
      }),
    ],
  ],

  themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
    ({
      image: 'img/removed_settings.png',
      colorMode: {
        disableSwitch: true,
        respectPrefersColorScheme: true,
      },
      navbar: {
        title: 'sbt-github',
        logo: {
          alt: 'sbt-github Logo',
          src: 'img/logo.svg',
        },
        items: [
          {
            type: 'doc',
            docId: 'getting-started',
            position: 'left',
            label: 'Docs',
          },
          {
            href: 'https://github.com/alejandrohdezma/sbt-github',
            className: "header-github-link",
            "aria-label": "GitHub repository",
            position: 'right',
          },
        ],
      },
      prism: {
        theme: require('prism-react-renderer/themes/github'),
        darkTheme: require('prism-react-renderer/themes/dracula'),
        additionalLanguages: ['java', 'scala'],
      },
    }),
};

module.exports = config;
