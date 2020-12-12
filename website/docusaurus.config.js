module.exports = {
  title: 'sbt-github',
  tagline: 'SBT plugin to read several settings from Github',
  url: 'https://alejandrohdezma.github.io',
  baseUrl: '/sbt-github/',
  onBrokenLinks: 'throw',
  onBrokenMarkdownLinks: 'warn',
  favicon: 'img/favicon.ico',
  organizationName: 'alejandrohdezma',
  projectName: 'sbt-github',
  themeConfig: {
    prism: {
      additionalLanguages: ['scala'],
    },
    sidebarCollapsible: false,
    navbar: {
      title: 'sbt-github',
      logo: {
        alt: 'sbt-github logo',
        src: 'img/logo.svg',
      },
      items: [
        {
          to: 'docs/',
          activeBasePath: 'docs',
          label: 'Documentation',
          position: 'right',
        },
        {
          href: 'https://github.com/alejandrohdezma/sbt-github',
          label: 'GitHub',
          position: 'right',
        },
      ],
    },
    footer: {
      style: 'dark',
      links: [
        {
          title: 'Documentation',
          items: [
            {
              label: 'Getting Started',
              to: 'docs/',
            },
            {
              label: 'Integrations',
              to: 'docs/sbt-mdoc',
            },
          ],
        },
        {
          title: 'Community',
          items: [
            {
              label: 'Github',
              href: 'https://github.com/alejandrohdezma',
            },
            {
              label: 'Twitter',
              href: 'https://twitter.com/alejandrohdezma',
            },
          ],
        },
        {
          title: 'More',
          items: [
            {
              label: 'GitHub',
              href: 'https://github.com/alejandrohdezma/sbt-github',
            },
          ],
        },
      ],
      copyright: `Copyright © ${new Date().getFullYear()} Alejandro Hernández`,
    },
    colorMode: {
      disableSwitch: true
    }
  },
  presets: [
    [
      '@docusaurus/preset-classic',
      {
        docs: {
          path: 'target/mdoc',
          editUrl: 'https://github.com',
          sidebarPath: require.resolve('./sidebars.js'),
        },
        theme: {
          customCss: require.resolve('./src/css/custom.css'),
        },
      },
    ],
  ],
};
